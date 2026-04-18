package gui.panel.buttons;

public interface Observable {

    public void removeObserver(Observer observer);

    public void updateObservers();

    public void addObserver(Observer observer);
}
