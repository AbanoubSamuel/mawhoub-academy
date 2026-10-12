package com.ficklebytes.service;


import com.ficklebytes.entity.Document;
import com.ficklebytes.exception.DocumentNotFoundException;
import com.ficklebytes.repository.DocumentRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  public Document GetDocumentById(int documentId) throws DocumentNotFoundException {
    Optional<Document> document = documentRepository.findById(documentId);
    if (!document.isPresent()) {
      throw new DocumentNotFoundException();
    }
    return document.get();
  }

  public Document Save(Document document) {
    return documentRepository.save(document);
  }

  public void Delete(Document document) {
    documentRepository.delete(document);
  }

  public Optional<Document> GetByDocumentNameAndContentId(String documentName, int contentId) {
    return documentRepository.findByDocumentNameAndContentId(documentName
        , contentId);
  }

}
