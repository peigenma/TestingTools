import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class SizeChangeListener implements ComponentListener {
	
	private Component[] verticalComp;
	private Component adaptiveComp;
	
	public SizeChangeListener(Component[] verticalComp, Component adaptiveComp) {
		this.verticalComp = verticalComp;
		this.adaptiveComp = adaptiveComp;
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent e) {
		int width = 0;
		for(Component comp : verticalComp) {
			width += comp.getWidth();
		}
		
		adaptiveComp.setSize(width+50, adaptiveComp.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

}
