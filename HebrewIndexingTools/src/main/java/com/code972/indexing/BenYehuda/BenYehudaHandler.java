package com.code972.indexing.BenYehuda;

/**
 * Created by Egozy on 05/11/2014.
 */
public abstract class BenYehudaHandler {
    public abstract void process(BenYehudaPage page);
    public void finishRemaining(){};
}
