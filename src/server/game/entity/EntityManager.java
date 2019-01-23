/*
    Manage all entities on server
    Written by Kane Templeton Jan 17, 2019
 */

package server.game.entity;


public class EntityManager {
    
    public static final int MAX_NUMBER_OF_ENTITIES = 10000;
    
    private Entity[] entities;
    private int curIndex;
    
    public EntityManager() {
        entities = new Entity[MAX_NUMBER_OF_ENTITIES];
        curIndex=0;
    }
    
    public void add(Entity E) {
        entities[curIndex++]=E;
    }
    
    public Entity get(int i) {
        if (i>=curIndex)
            return null;
        return entities[i];
    }
    
    public int getIndex() {return curIndex;}

}
