package br.com.atlas.model;

public class Author {
    private int authorId;
    private String authorName;

    // Construtor para quando você vai CADASTRAR (ainda não tem ID do banco)
    public Author(String authorName) {
        this.authorName = authorName;
    }

    // Construtor para quando você busca do BANCO (já vem com ID)
    public Author(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    // Getters e Setters
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}