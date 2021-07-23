package util;

import java.util.List;
import java.util.ArrayList;

public interface Observable {
    default public List<Observer> getObservers() {
        return new ArrayList<>();
    }

    default public void notifyObservers() {
        for ( Observer observer : getObservers() ) observer.update(this);
    }

    default public void addObserver(Observer o) {
        this.getObservers().add(o);
    }
    
    default public void removeObserver(Observer o) {
        this.getObservers().remove(o);
    }
}
