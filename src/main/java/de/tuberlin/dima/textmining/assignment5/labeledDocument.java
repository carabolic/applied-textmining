package de.tuberlin.dima.textmining.assignment5;

/**
 * Created by IntelliJ IDEA.
 * User: Christoph Boden
 * Date: 14.11.11
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class labeledDocument {

    private final String document_text;
    private final String document_label;

    public  labeledDocument(String document_text, String document_label){
        this.document_label = document_label;
        this.document_text = document_text;
    }

    public String getText(){
        return this.document_text;
    }

    public String getLabel() {
        return this.document_label;
    }
}
