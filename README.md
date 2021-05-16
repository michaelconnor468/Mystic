# Mystic
2D Open World Java Game. To run, simply execute the shell script entitiled run in this folder which will build and execute the current version of the game.

## Technical Details

The game is built and tested using gradle and it's directory structure matches the standard structure emplyed by gradle. A lot of the java files themselves contain some documentaion in the form of javadoc which the reader is free to reference. Some higher level details will be provided below as well.

### Entities and Chunks

Entities such as tiles, trees, and the player can move, be rendered and interact with each other. They are all stored within their respective chunks which are loaded and unloaded on demand due to the infinite nature of the map requiring a way to acheive stable performace on all map sizes. Entities such as floor tiles are fully customizable and stored in plain JSON for easy addition and modding. This allows the player to mould their map to an infinite array of combinations as the whole world is made to be fully modifiable.

#### Entities

Entities are each assosiated with a chunk, with the player being a special external entity. They are free to move around and collide with each other through the use of the CollisionBox inner class. An entity can have more than one collision box for fine grained control. Upon moving, entities query other entities within a optimized radius and then tests for collisions. Entities are stored within specialized storage containers within each chunk to allow optimizer querying and storage for the particular type of entity.

#### Chunks

Chunks are the highest level of storage container which store a given square area of entities. It is chunks that dispatch timing ticks and render requests to their contained entities and which save and load themselves and their related entities. Entities may freely move across chunks which are managed by the Chunk Manager. The Chunk Manager both loads and unloads chunks and their respective entities based on the position of the player. This allows maps to be infinite in size while still using the same number of resources.

### Storage

All entities and settings are saved in JSON. This allows for ease of reading and modification of game files. Additionally the game uses its own proprietary JSON parser with a smoother syntax. There is not much reason for this other than me thinking of a cool parser design I was dying to implement.

#### Parsing

Parsing is performed through object-oriented parsers which produce parse object wrappers ranging from primitive values to arrays to blocks. JSON is passed into parsers which output such parser objects. Parsers can also contain helper information such as the number of lines parsed which is why they are instantiated as individual objects.

```java
ParserBlock block = (new BlockParser()).parse("{ nums: [1, 3], num2: 6 }");
int num2 = ((ParserInt) block.getProperties().get("num2")).getNumber();
Iterator<ParserObject> nums = ((ParserArray) block.getProperties().get("nums")).iterator();
```

#### Saving and Loading

Entities can be saved and loaded through their own unique implementation of static save and load methods. This is done through the input and output of JSON that is used to save and load an entity. All entities except the player are saved and loaded from the chunk file corresponding to the chunk they are currently inside of.

### Serivces and Context

Services and global variables are grouped together in a context object named X. This is generally passed around through dependancy injection and can be used by any class to access a global variable or service such as to register itself with a timing manager or look into the game's settings.

### Timing

The game makes heavy use of asynchronous logic over a synchronous main loop. Timing managers are used to create ticks for various logical components to receive at given intervals and perform actions during. In general, frequency of timing is normalized against ticks per second to keep behaviour of components consistant and unnafected by timing changes. Additionally, timing ticks may be split into separate threads for safe components to enable easy multi-threading within the application. 

#### Timing Manager

Main timing manager for components to register with. Keeps tracks of components that are registered to it and on each tick pulse calls the tick method of its registered components so that they may execute their logic. As few components are tied to the timing manager as possible, and a heirarchy of delegation is preferred to have timing of subcomponents depend on that of their parent. All components that register with this execute on a separate thread from the main game. The tick method also takes in a fresh context object if the old one is for whatever reason replaced.

#### Render Manager

Timing for rendering is distinct from timing for the rest of the components due to JavaFX not being thread safe. All render timing is handled on the main JavaFX thread by the Render Manager and is not related to the ticks of the timing manager.

#### Example

Registering logic for timing requres extending either TickObserver or Renderable based on which timing interface is used. The object then need only register with each respective timing manager or with its parent component that is linked to some timing manager through other components or directly.

```java
class MyClass implements TickObserver, Renderable {
    public MyClass(X x) {
        x.getTimingManager().register(this);
        x.getRenderManager().register(this);
    }

    public void tick(X x) { System.out.println("Tick"); }
    public void render(Renderer r) { System.out.println("Render"); }
}
```

### Rendering

Rendering consists of drawing animations to the screen at timed intervals. Only entities may animate themselves though the render implementation does not enforce it. This is used over a RenderableEntity derived class to keep inheritance chains short.

#### Animation

An animation is a collection of images which are cycled through as the game progresses and can be drawn to the screen by a renderer object. The animation provides a single image to be rendered to the renderer at any given time which can change based on the game's main timer.

#### Renderer

The renderer is an object entities can use to register an animation for drawing on a given render cycle. The renderer decouples rendering logic from entities and performs calculations on where to render an image from an animation relative to the player on the screen and decides whether drawing is needed based on whether the image is on or off the screen. The renderer also does any ordering of entities based on vertical position in order to provide the perception of depth and decouple the need to manage z-indices directly. This allows for things such as roofs and trees to naturally render where they need to go with ease. 

#### Render Manager

The render manager handles timing related to the renderer and calls entities that register with it to supply an animation for render in a given cycle. Once all such animations are collected, the render manager lets the renderer know it is time to draw onto the screen in order to ensure all animations are registered for render before drawing begins so that the renderer may order and pre-process entities accordingly.

#### Example

An entity that wants to render animations to the screen must implement the Renderable.java interface and register itself with the game's render manager. It is then called to provide an animation on each render cycle. An animation is created using the path of the image containing subimages to animate and the entity which it is animating.

```java
class MyEntity extends Entity implements Renderable {
    Animation animation;

    public MyEntity(X x, Path animationPath) {
        x.getRenderManager().register(this);
        animation = new Animation(x, this, animationPath);
    }

    public void render(Renderer r) { r.render(animation); }
}

```
