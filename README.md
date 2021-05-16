# Mystic
2D Open World Java Game. To run, simply execute the shell script entitiled run in this folder which will build and execute the current version of the game.

## Technical Details

The game is built and tested using gradle and it's directory structure matches the standard structure emplyed by gradle. A lot of the java files themselves contain some documentaion in the form of javadoc which the reader is free to reference. Some higher level details will be provided below as well.

### Rendering

Rendering consists of drawing animations to the screen at timed intervals.

#### Animation

An animation is a collection of images which are cycled through as the game progresses and can be drawn to the screen by a renderer object. The animation provides a single image to be rendered to the renderer at any given time which can change based on the game's main timer.

#### Renderer

The renderer is an object entities can use to register an animation for drawing on a given render cycle. The renderer decouples rendering logic from entities and performs calculations on where to render an image from an animation relative to the player on the screen and decides whether drawing is needed based on whether the image is on or off the screen. The renderer also does any ordering of entities based on vertical position in order to provide the perception of depth and decouple the need to manage z-indices directly. This allows for things such as roofs and trees to naturally render where they need to go with ease. 

#### Render Manager

The render manager handles timing related to the renderer and calls entities that register with it to supply an animation for render in a given cycle. Once all such animations are collected, the render manager lets the renderer know it is time to draw onto the screen in order to ensure all animations are registered for render before drawing begins so that the renderer may order and pre-process entities accordingly.
