# API inventory (P0.3)

Generated 2026-10-13 from `src/main/java/com/ficklebytes/controller/*` (baseline `9d2fd61`) and the Angular services in `../FE/src/app`.
Use it to check that every endpoint still exists with the same method and path after each migration phase.

- Backend endpoints: **75** across 16 controllers (`ExceptionHandlerController` has no mappings).
- Frontend HTTP calls found: **62**. **None use a trailing slash**, so the Spring 6 trailing-slash change doesn't affect this FE.
- FE base URL (`environment.apiUrl`): mawhoub `dz` and `dzstaging` use `https://api.educameroon.com/api`. `ng` uses `https://www.ngeducators.com` (no `/api`).

## FE calls with no matching BE endpoint (pre-existing)

- `POST /api/email/contact` (called at src/app/service/users.service.ts:76)

## Endpoints

| Method | Path | Handler | Used by FE |
|---|---|---|---|
| POST | `/api/auth/change-password` | AuthController#changePassword | **no** |
| GET | `/api/auth/is-email-used` | AuthController#isEmailUsed | yes |
| POST | `/api/auth/login` | AuthController#loginFromUsernameAndPassword | yes |
| POST | `/api/auth/login-from-facebook` | AuthController#loginFromFacebook | yes |
| POST | `/api/auth/register` | AuthController#register | yes |
| POST | `/api/auth/reset-password` | AuthController#resetPassword | yes |
| GET | `/api/auth/send-reset-token` | AuthController#sentPasswordResetToken | yes |
| GET | `/api/auth/verify-user-token` | AuthController#verifyUserToken | yes |
| POST | `/api/card/create` | CardController#create | yes |
| DELETE | `/api/card/delete` | CardController#delete | yes |
| DELETE | `/api/card/delete-all` | CardController#deleteAll | yes |
| GET | `/api/card/list` | CardController#list | yes |
| POST | `/api/comment/create` | CommentController#add | yes |
| DELETE | `/api/comment/delete` | CommentController#delete | yes |
| GET | `/api/comment/list` | CommentController#list | yes |
| POST | `/api/comment/mark-comment-as-seen-by-teacher/{courseId}/{commentId}` | CommentController#markCommentAsSeenByTeacher | yes |
| GET | `/api/configuration/list` | BusinessInfoController#getConfiguration | **no** |
| POST | `/api/configuration/update` | BusinessInfoController#updateConfiguration | **no** |
| POST | `/api/content/change-order` | ContentController#changeOrder | yes |
| POST | `/api/content/clone-contents-to-courses` | ContentController#cloneContentsToCourses | yes |
| POST | `/api/content/create-lesson` | ContentController#createLesson | yes |
| POST | `/api/content/create-unit` | ContentController#createUnit | yes |
| DELETE | `/api/content/delete` | ContentController#delete | yes |
| POST | `/api/content/edit-lesson` | ContentController#editLesson | yes |
| POST | `/api/content/edit-unit` | ContentController#editUnit | yes |
| GET | `/api/content/get` | ContentController#get | yes |
| GET | `/api/content/list` | ContentController#list | yes |
| GET | `/api/content/list-with-unseen-comments-count/{courseId}` | ContentController#listWithUnseenCommentsCount | yes |
| GET | `/api/content/next` | ContentController#next | yes |
| GET | `/api/content/previous` | ContentController#previous | yes |
| GET | `/api/course/author/{authorId}` | CourseController#getTeacherCourses | yes |
| POST | `/api/course/create` | CourseController#create | yes |
| DELETE | `/api/course/delete` | CourseController#delete | yes |
| POST | `/api/course/edit` | CourseController#edit | yes |
| GET | `/api/course/get` | CourseController#get | yes |
| GET | `/api/course/getBySchoolId` | CourseController#getBySchoolId | yes |
| GET | `/api/course/list-all` | CourseController#listAll | yes |
| GET | `/api/course/list-public` | CourseController#listPublic | **no** |
| DELETE | `/api/document/delete` | DocumentController#delete | yes |
| ANY | `/api/document/download` | DocumentController#download | **no** |
| POST | `/api/document/upload` | DocumentController#upload | yes |
| POST | `/api/email/send` | EmailController#sendEmail | **no** |
| GET | `/api/general/current-version` | GeneralController#returnVersion | **no** |
| GET | `/api/general/minimum-version` | GeneralController#returnMinimumVersion | **no** |
| GET | `/api/lecture/author/{authorId}` | LectureController#getLectureLectures | yes |
| POST | `/api/lecture/create` | LectureController#create | yes |
| DELETE | `/api/lecture/delete` | LectureController#delete | **no** |
| POST | `/api/lecture/edit` | LectureController#edit | yes |
| GET | `/api/lecture/get` | LectureController#get | yes |
| GET | `/api/lecture/getByStudyId` | LectureController#getByStudyId | yes |
| GET | `/api/lecture/list-all` | LectureController#listAll | yes |
| GET | `/api/lecture/list-public` | LectureController#listPublic | **no** |
| GET | `/api/lecturer/get-lecturer-info` | LecturerController#getLecturerInfo | **no** |
| GET | `/api/lecturer/list-lecturers` | LecturerController#returnAllLecturers | **no** |
| POST | `/api/lecturer/update-lecturer-info` | LecturerController#updateLecturerInfo | **no** |
| POST | `/api/lecturer/update-lecturer-picture` | LecturerController#updateLecturerPicture | **no** |
| POST | `/api/purchase/activate` | PurchaseController#activate | yes |
| GET | `/api/purchase/course/{courseId}` | PurchaseController#listPurchasesForCourse | yes |
| GET | `/api/purchase/is-purchased` | PurchaseController#purchased | yes |
| GET | `/api/purchase/list-my-purchases` | PurchaseController#listPurchases | yes |
| POST | `/api/school/add` | SchoolController#add | yes |
| DELETE | `/api/school/delete` | SchoolController#delete | yes |
| GET | `/api/school/list-all` | SchoolController#get | yes |
| POST | `/api/specialty/add` | SpecialtyController#add | yes |
| DELETE | `/api/specialty/delete` | SpecialtyController#delete | yes |
| GET | `/api/specialty/list-all` | SpecialtyController#get | yes |
| POST | `/api/study/add` | StudyController#add | yes |
| DELETE | `/api/study/delete` | StudyController#delete | yes |
| GET | `/api/study/list-all` | StudyController#get | yes |
| GET | `/api/user/get-teacher-info` | UserController#getTeacherInfo | yes |
| GET | `/api/user/list-all` | UserController#getUsers | yes |
| GET | `/api/user/list-teachers` | UserController#returnAllTeachers | yes |
| POST | `/api/user/update-teacher-info` | UserController#updateTeacherInfo | yes |
| POST | `/api/user/update-teacher-picture` | UserController#updateTeacherPicture | yes |
| POST | `/api/user/update-user-role` | UserController#updateUserRole | yes |

14 backend endpoints aren't called by this FE. They may be used by a mobile app or other clients (`/api/general/*-version` suggests a mobile app), so keep them all.
