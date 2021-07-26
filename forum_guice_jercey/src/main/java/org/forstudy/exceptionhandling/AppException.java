package org.forstudy.exceptionhandling;

public class AppException extends Exception {

    private Integer status;

    private String link;

    private String developerMessage;

    public AppException(int status, String message,
                        String developerMessage, String link) {
        super(message);
        this.status = status;
        this.developerMessage = developerMessage;
        this.link = link;
    }

    public AppException() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
