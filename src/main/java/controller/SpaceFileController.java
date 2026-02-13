/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import model.data.SpaceDataFile;
import model.entities.Space;

/**
 *
 * @author 50687
 */
public class SpaceFileController {

    SpaceDataFile spaceData;
    
    public SpaceFileController() throws IOException{
        spaceData = new SpaceDataFile();
    }
    
    public int getNextId() throws IOException, NullPointerException {
        return spaceData.getNextId();
    }
    
    public Space getSpaceFromFile(int id) throws IOException {
        return spaceData.getSpaceFromFile(id);
    }
    
    public void insertSpace(Space space) throws IOException {
        spaceData.insertSpace(space);
    }
}
    
    
