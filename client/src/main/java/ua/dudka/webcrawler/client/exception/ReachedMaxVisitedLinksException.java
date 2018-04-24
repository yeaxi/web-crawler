package ua.dudka.webcrawler.client.exception;

import lombok.Getter;

@Getter
public class ReachedMaxVisitedLinksException extends RuntimeException {

    private static final String MESSAGE = "Can't add link '%s'. Reached max visited links for task with id: %s";
    private final String id;
    private final String link;

    public ReachedMaxVisitedLinksException(String id, String link) {
        super(String.format(MESSAGE, link, id));
        this.id = id;
        this.link = link;
    }
}