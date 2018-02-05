package gabaritos;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.paim.commons.BinaryImage;
import org.paim.orchestration.StructureType;

/**
 * Template slice
 */
public class SliceTemplate {

    /** Last update date */
    private final Date lastUpdate;
    /** Structures */
    private final Map<StructureType, BinaryImage> structures;

    /**
     * Creates a new slice template
     * 
     * @param lastUpdate
     */
    public SliceTemplate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        this.structures = new HashMap<>();
    }

    /**
     * Returns the last update date 
     * 
     * @return Date
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Returns the structures of the slice
     * 
     * @return Map
     */
    public Map<StructureType, BinaryImage> getStructures() {
        return Collections.unmodifiableMap(structures);
    }

    /**
     * Adds or replaces the image linked to the structure type
     * 
     * @param type
     * @param image
     * @return BinaryImage
     */
    public BinaryImage putStructure(StructureType type, BinaryImage image) {
        return structures.put(type, image);
    }
    
}
