package gabaritos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Template
 */
public class Template {

    /** ID */
    private final int id;
    /** Template's sequence */
    private final int sequence;
    /** Template's author */
    private final String author;
    /** Template's creation */
    private final Date creation;
    /** Template's slices */
    private final List<SliceTemplate> slices;

    /**
     * Creates a new template
     *
     * @param id
     * @param sequence
     * @param author
     * @param creation
     */
    public Template(int id, int sequence, String author, Date creation) {
        this.id = id;
        this.sequence = sequence;
        this.author = author;
        this.creation = creation;
        this.slices = new ArrayList<>();
    }

    /**
     * ID
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Templates sequence
     *
     * @return int
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Templates author
     *
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Templates creation
     *
     * @return Date
     */
    public Date getCreation() {
        return creation;
    }

    /**
     * Returns the template slices
     * 
     * @return List
     */
    public List<SliceTemplate> getSlices() {
        return slices;
    }

}
