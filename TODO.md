# Priority 1
- Add inventory functionality

## Priority 2
- Add build menu and functionality for tiles
- Fix issue with swimming too close to land
- Fix issue with items not being easily picked up

### Priority 3
- Refactor chunk constructor into helper method

### To Investigate

Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException
	at java.base/java.util.ArrayList.sort(ArrayList.java:1723)
	at java.base/java.util.Collections.sort(Collections.java:179)
	at game.entities.containers.EntityContainer.indexEntities(EntityContainer.java:44)
	at game.entities.containers.EntityContainer.addEntity(EntityContainer.java:33)
	at game.entities.Chunk.addEntity(Chunk.java:90)
	at game.main.ChunkManager.addEntity(ChunkManager.java:199)
	at game.player.weapons.MeleeWeapon.use(MeleeWeapon.java:14)
	at game.player.Player.onClick(Player.java:51)
	at views.scenes.PlayingScene.lambda$setupKeystrokes$2(PlayingScene.java:138)
	at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:238)
	at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191)
	at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
	at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
	at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
	at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
	at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:54)
	at javafx.event.Event.fireEvent(Event.java:198)
	at javafx.scene.Scene$ClickGenerator.postProcess(Scene.java:3569)
	at javafx.scene.Scene$MouseHandler.process(Scene.java:3871)
	at javafx.scene.Scene.processMouseEvent(Scene.java:1849)
	at javafx.scene.Scene$ScenePeerListener.mouseEvent(Scene.java:2590)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:409)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:299)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:391)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler.lambda$handleMouseEvent$2(GlassViewEventHandler.java:447)
	at com.sun.javafx.tk.quantum.QuantumToolkit.runWithoutRenderLock(QuantumToolkit.java:412)
	at com.sun.javafx.tk.quantum.GlassViewEventHandler.handleMouseEvent(GlassViewEventHandler.java:446)
	at com.sun.glass.ui.View.handleMouseEvent(View.java:556)
	at com.sun.glass.ui.View.notifyMouse(View.java:942)
	at com.sun.glass.ui.gtk.GtkApplication._runLoop(Native Method)
	at com.sun.glass.ui.gtk.GtkApplication.lambda$runLoop$11(GtkApplication.java:277)
	at java.base/java.lang.Thread.run(Thread.java:831)

