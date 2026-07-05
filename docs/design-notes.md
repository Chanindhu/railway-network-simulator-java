# Design Notes

## Overview

The railway network simulator models towns that produce goods and railways that move goods between towns. The simulation receives generated messages for new towns, population changes, railway construction, and railway duplication. It then updates the network state over simulated days.

## Package Responsibilities

### `edu.curtin.app.core`
Contains the main simulation loop and the input provider abstraction.

### `edu.curtin.app.domain`
Contains the core domain objects, including towns and railway links.

### `edu.curtin.app.domain.state`
Contains state objects that define railway behaviour.

### `edu.curtin.app.factory`
Contains factory interfaces and default factory implementations for towns, railway links, and railway states.

### `edu.curtin.app.manager`
Contains managers for towns, railways, and goods transportation.

### `edu.curtin.app.messages`
Contains generic message types and data models used by the simulation input system.

### `edu.curtin.app.observer`
Contains observer interfaces and reporting classes that react to simulation events.

### `edu.curtin.app.logger`
Contains logging abstractions and file logging implementation.

## Observer Pattern

The simulation uses an observer-style design to reduce coupling between the simulation loop and the parts of the system that react to events.

The `Simulation` class notifies observers when:

- a day starts,
- a new input message is received,
- a day ends.

Managers and reporters implement `SimulationObserver`, allowing each component to handle its own responsibility without the simulation needing to know the internal details.

## State Pattern

Railway behaviour changes depending on its current state:

- `UnderConstructionState`
- `SingleTrackState`
- `DualTrackState`

The `RailwayLink` delegates state-specific behaviour to a `RailwayState` object. This keeps conditional logic out of the railway model and makes the behaviour easier to extend.

## Factories and Dependency Injection

The application uses constructor-based dependency injection in `App.java` to wire managers, factories, loggers, and observers together.

Factory interfaces are used for creating:

- towns,
- railway links,
- railway states.

This keeps object creation separate from the business logic and makes the code easier to test or extend.

## Generics

The `Message<T>` class uses generics to support type-safe message payloads for different event types such as town data and railway data.

## Logging

The application writes timestamped logs to `simulation.log`. Different log levels are used to separate informational messages, debugging details, warnings, and errors.
