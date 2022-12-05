/**
 * This is an interface that defines how a callback should be handled when the drop down panel
 * close action is requested
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.event.ActionEvent;
import java.util.EventListener;

public interface DropDownPanelCloseAction extends EventListener{
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e);
}
