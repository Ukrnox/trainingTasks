package org.forstudy.exceptionhandling;

public class ErrorMessage {

    private int status;

    private String message;

    private String developerMessage;

    private String link;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public ErrorMessage(AppException ex){
        this.message = ex.getMessage();
        this.link = ex.getLink();
        this.status = ex.getStatus();
        this.developerMessage = ex.getDeveloperMessage();
    }

//    public ErrorMessage(NotFoundException ex){
//        this.status = Response.Status.NOT_FOUND.getStatusCode();
//        this.message = ex.getMessage();
//        this.link = "https://jersey.java.net/apidocs/2.8/jersey/javax/ws/rs/NotFoundException.html";
//    }

    public ErrorMessage() {}
}
