package com.library.domain;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="issuance")
public class Issuance {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long bookId;

  private String bookName;

  private String authorName;

  @CreatedDate
  private Date issueDate;

  private String studentUsn;

  public Issuance() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public String getStudentUsn() {
    return studentUsn;
  }

  public void setStudentUsn(String studentUsn) {
    this.studentUsn = studentUsn;
  }
}
