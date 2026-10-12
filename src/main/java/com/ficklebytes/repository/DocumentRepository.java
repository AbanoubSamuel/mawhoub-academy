package com.ficklebytes.repository;


import com.ficklebytes.entity.Document;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository("DocumentRepository")
public interface DocumentRepository extends CrudRepository<Document, Integer> {

  List<Document> getAllByContentId(int contentId);

  Optional<Document> findByDocumentNameAndContentId(String documentName,
      int contentId);

}
