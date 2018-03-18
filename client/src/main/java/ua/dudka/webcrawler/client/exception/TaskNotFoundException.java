package ua.dudka.webcrawler.client.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String taskId) {
        super("Task with id: " + taskId + " was not found!");
    }
}