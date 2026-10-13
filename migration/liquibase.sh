#!/usr/bin/env bash
# Run a Liquibase Maven goal against one environment's database, without starting the app.
#
#   migration/liquibase.sh dev status --verbose
#   migration/liquibase.sh dev update
#   CONFIRM_PROD=yes migration/liquibase.sh prod status
#
# Connection settings come from secrets.local.env (<ENV>_DB_URL, <ENV>_DB_USERNAME, <ENV>_DB_PASSWORD).
# The file is parsed, never sourced: values may contain shell metacharacters such as ';'.
set -euo pipefail

cd "$(dirname "$0")/.."

env_name="${1:?usage: $0 <dev|prod> <goal> [extra liquibase args]}"
goal="${2:?usage: $0 <dev|prod> <goal> [extra liquibase args]}"
shift 2

if [[ "$env_name" == "prod" && "${CONFIRM_PROD:-}" != "yes" ]]; then
  echo "Refusing to run against prod without CONFIRM_PROD=yes" >&2
  exit 1
fi

prefix="$(tr '[:lower:]' '[:upper:]' <<< "$env_name")"
props_name="liquibase-${env_name}.properties"
props="target/liquibase/${props_name}"
mkdir -p target/liquibase
trap 'rm -f "$props"' EXIT
(umask 077 && python3 -I - "$prefix" secrets.local.env "$props" <<'EOF'
import sys
prefix, env_file, out = sys.argv[1:]
values = {}
for line in open(env_file, encoding="utf-8"):
    key, sep, value = line.rstrip("\n").partition("=")
    if sep and not key.lstrip().startswith("#"):
        values[key.strip()] = value
missing = [k for k in ("URL", "USERNAME", "PASSWORD") if not values.get(f"{prefix}_DB_{k}")]
if missing:
    sys.exit(f"secrets.local.env is missing {', '.join(f'{prefix}_DB_{k}' for k in missing)}")
escape = lambda v: v.replace("\\", "\\\\").replace(":", "\\:").replace("=", "\\=")
with open(out, "w", encoding="utf-8") as f:
    f.write(f"url={escape(values[prefix + '_DB_URL'])}\n")
    f.write(f"username={escape(values[prefix + '_DB_USERNAME'])}\n")
    f.write(f"password={escape(values[prefix + '_DB_PASSWORD'])}\n")
EOF
)

mvn -B -q "liquibase:${goal}" -Dliquibase.propertyFile="$props_name" "$@"
