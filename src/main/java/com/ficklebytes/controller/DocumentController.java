package com.ficklebytes.controller;

import com.ficklebytes.entity.AcademyUser;
import com.ficklebytes.entity.Document;
import com.ficklebytes.exception.DocumentNotFoundException;
import com.ficklebytes.exception.UnAuthorizedException;
import com.ficklebytes.service.DocumentService;
import com.ficklebytes.service.UserService;
import com.ficklebytes.utils.AuthUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
@RequestMapping("/api/document")
public class DocumentController {

  private static final String CUSTOM_DOCUMENTS_MEDIA = "documents/";
  @Autowired
  private DocumentService documentService;
  @Autowired
  private UserService userService;

  @PostMapping(value = "/upload")
  @ResponseBody
  public ResponseEntity upload(@RequestParam(value = "contentId") int contentId,
      @RequestParam(value = "documentName") String documentName,
      @RequestParam(value = "file") MultipartFile file,
      @AuthenticationPrincipal AcademyUser academyUser) throws UnAuthorizedException {
    AuthUtil.requiresAdminRole(academyUser);

    if(Objects.isNull(documentName)){
      documentName = file.getOriginalFilename();
    }
    CreateFolderIfNotExist(CUSTOM_DOCUMENTS_MEDIA);
    CreateFolderIfNotExist(CUSTOM_DOCUMENTS_MEDIA + File.separator + contentId);
    Optional<Document> document =
        documentService.GetByDocumentNameAndContentId(documentName,
            contentId);
    if (!document.isPresent()) {
      document =
          Optional.of(Document.builder().contentId(contentId).documentName(documentName).build());
    }
    boolean fileUploaded = UploadFile(file, ConstructPath(contentId,
        documentName));
    if (!fileUploaded) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok(documentService.Save(document.get()));
  }

  private void CreateFolderIfNotExist(String path) {
    File f = new File(path);
    if (!f.exists()) {
      f.mkdir();
    }
  }

  private String getFileExtension(MultipartFile file) {
    String name = file.getName();
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return "."; // empty extension
    }
    return name.substring(lastIndexOf);
  }

  public boolean UploadFile(MultipartFile file, String pathUri) {
    try {
      byte[] bytes = file.getBytes();
      Path path = Paths.get(pathUri);
      Files.write(path, bytes);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private String ConstructPath(int contentId, String fileName) {
    return CUSTOM_DOCUMENTS_MEDIA + File.separator + contentId + File.separator + fileName;
  }

  @DeleteMapping(value = "/delete")
  public ResponseEntity delete(@AuthenticationPrincipal AcademyUser academyUser,
      @RequestParam("documentId") int documentId)
      throws UnAuthorizedException, DocumentNotFoundException {
    AuthUtil.requiresAdminRole(academyUser);

    Document document = documentService.GetDocumentById(documentId);
    File documentFile = new File(ConstructPath(document.getContentId(),
        document.getDocumentName()));
    documentService.Delete(document);
    if (documentFile.delete()) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }

  @RequestMapping(value = "/download")
  @ResponseBody
  public ResponseEntity<InputStreamResource> download(@RequestParam(
      "contentId") int contentId,
      @RequestParam(
          "filename") String fileName) throws FileNotFoundException {
    File file = new File(ConstructPath(contentId, fileName));
    InputStreamResource resource =
        new InputStreamResource(new FileInputStream(file));
    ContentDisposition contentDisposition = ContentDisposition.builder(
        "inline")
        .filename(file.getName())
        .build();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentDisposition(contentDisposition);
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentLength(file.length());

    return ResponseEntity.ok()
        .headers(headers)
        .body(resource);
  }


}
