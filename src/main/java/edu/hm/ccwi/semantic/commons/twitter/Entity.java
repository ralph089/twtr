package edu.hm.ccwi.semantic.commons.twitter;


import org.apache.commons.lang3.Validate;

/**
 * The type Entity.
 */
public class Entity {

    private final String entity;
    private final String type;

    /**
     * Instantiates a new Entity.
     *
     * @param entity the entity
     * @param type   the type
     */
    public Entity(String entity, String type) {
        this.entity = Validate.notBlank(entity);
        this.type = Validate.notBlank(type);
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }
}
