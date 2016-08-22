/******************************************************************************
 SessionLog OmegaT plugin - Plugin for OmegaT (htpp://www.omegat.org) to track
                            the actions of a user during translation and storing
                            the log in an XML file.
                            This plugin keeps track of all the editions
                            performed by a user during the translation of a
                            project. The plugin works in a transparent way for
                            the user and all the information obtained is stored
                            in an XML file which can be created when the tool
                            is closed.

 Copyright (C) 2013-2014 Universitat d'Alacant [www.ua.es]

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 **************************************************************************/

package org.omegat.plugins.sessionlog.loggers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Stack;
import org.omegat.core.data.SourceTextEntry;

/**
 *
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public interface BaseLogger {
    
    public class UndoManager{
        private Stack<Integer> undoable_idx;

        private Stack<Integer> redoable_idx;

        public Stack<Integer> getUndoableIdx() {
            return undoable_idx;
        }

        public Stack<Integer> getRedoableIdx() {
            return redoable_idx;
        }
        
        public UndoManager(){
            undoable_idx=new Stack<Integer>();
            redoable_idx=new Stack<Integer>();
        }
    
        public int Undo(){
            if(!undoable_idx.isEmpty()){
                int editionid=undoable_idx.pop();
                redoable_idx.push(editionid);
                return editionid;
            }
            else
                return -1;
        }

        public int Redo(){
            if(!redoable_idx.isEmpty()){
                int editionid=redoable_idx.pop();
                undoable_idx.push(editionid);
                return editionid;
            }
            else
                return -1;
        }
    }
    
    public abstract int getCurrentTMProposals();

    public abstract void setCurrentTMProposals(int current_tm_proposal);

    public abstract void setEmtpyGlossaryProposals(boolean emtpy_glossary_proposals);

    public abstract void setEmtpyMTProposals(boolean emtpy_mt_proposals);

    public abstract void setEmtpyTMProposals(boolean emtpy_tm_proposals);
    
    public abstract void LoggerEvent(String code, String param, String message);
    
    public abstract void GenericEvent(String eventType, String code, String param, String message);
    
    public abstract void Undo();

    public abstract void Redo();
    
    public abstract void SetLastEditedText(String newtext);
        
    public abstract void SetPause(long increment);
    
    public abstract int GetCurrentSegmentNumber();
    
    public abstract void NewProject() throws FileNotFoundException;
    
    public abstract void CloseProject();
    
    public abstract void NewFile(String doc_name);
    
    public abstract void CloseEntry();
    
    public abstract void NewEntry(SourceTextEntry active_entry);
    
    public abstract void DumpToWriter(PrintWriter pw);
    
    public abstract void NewInsertion(int offset, String text);
    
    public abstract void NewDeletion(int offset, String text);

    public abstract void InsertFromTM(int offset, int tu_pos, String text,
            int fms_stemming_onlywords, int fms_onlywords, int fms);
    
    public abstract void ReplaceFromTM(int offset, int tu_pos,
            String removedtext, String insertedtext, int fms_stemming_onlywords,
            int fms_onlywords, int fms);

    public abstract void ReplaceFromMT(int offset, String removedtext,
            String newtext);

    public abstract void InsertFromGlossary(int offset, String newtext);

    public abstract void CaretUpdate(int init_selection, int end_selection);
    
    public abstract void Reset();
}
