package br.com.atlas.model;

import java.util.ArrayList;
import java.util.List;
//EM IMPLEMENTAÇÃO!!!
public class Collection {

    public List<Book> searchBookByTitle(String title) {
        
        List<Book> temp = new ArrayList<>();
        for (Book b : books) {
            if (b.getBookName().contains(title)) {
                temp.add(c);
            }
        }
        return temp;
    }

    public Book searchBookByCpf(String cpf) {
        
        for (Book c : books) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }
}