package com.rescueplanner.model;

public class AssessmentFeedback {
  private String moes;
  private String mops;
  private String reviewTriggers;
  private String learningLog;
  private String draft;

  public AssessmentFeedback() {
  }

  public String getMoes() {
    return moes;
  }

  public void setMoes(String moes) {
    this.moes = moes;
  }

  public String getMops() {
    return mops;
  }

  public void setMops(String mops) {
    this.mops = mops;
  }

  public String getReviewTriggers() {
    return reviewTriggers;
  }

  public void setReviewTriggers(String reviewTriggers) {
    this.reviewTriggers = reviewTriggers;
  }

  public String getLearningLog() {
    return learningLog;
  }

  public void setLearningLog(String learningLog) {
    this.learningLog = learningLog;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
