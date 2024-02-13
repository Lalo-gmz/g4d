package mx.lania.g4d.service.utils;

public class GitLabProjectNameAlreadyExistException extends RuntimeException {

    public GitLabProjectNameAlreadyExistException(String message) {
        super(message);
    }
}
