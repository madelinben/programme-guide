# Sabaton EPG

This project displays an Electronic Programme Guide for 12 Freeview channels. It will show data for the past 24 hours, today and the next 2 days. All of the data is live data taken from [Bleb]("http://bleb.org/tv/data/listings/").

This project can cycle through all of these programmes, allow you to see what is on now and next.

It will also allow you to mark the programmes to record based on whether there is a tuner to record it on. if not then it will advise you when it is next being shown to allow you to record it then.

The project's entry point is com.sabaton.Controller.HomeController.main().

## Custom Feature

For the custom feature part of our project we have chosen to add Day Skipping, this will move the grid forward 1 day, and then a Favourites section which will allow the user to auto record their favourites.

## 3rd Party Libraries Used

For all 3rd party libraries we used we pulled them down from Maven. These are:

- joda-time
- junit
- json-simple
- Apache Commons IO

## Standards in the Code

### Coding

#### Branch Names

Branch names must follow convention of `feature/[SPRINT_NAME]`. The Comment must be short description of what you are doing on the branch.

> Each word in the sprint name must be separated by a hyphen. for example feature/epg-data-persistence.

> If you are doing a bugfix then replace feature in the name with bugfix. This applies for all the types of branches below.

##### Types

- Bugfix - Issue found during testing and development
- Hotfix - Client has found an issue
- Feature - New feature being added to system

#### Pull Requests

The Pull Request must have Work Items linked to it relating to the commits that you have done. This should be done automatically. Please also add the commit messages to the Pull Request.

##### Description

The description of the Pull Request must follow the following format.

```
# Changes

- Change 1
- Change 2

## Commits

Commit Messages Here
```

#### Commits

Commits should have at least 1 Work Item attached to it.

#### Commenting

In the solution please comment above the method using the javadocs format

```java
/**
* [METHOD_DESCRIPTION]
* @param    [PARAMETER_NAME]    [PARAMETER_DESCRIPTION]
* @return                       [RETURN_VALUE_DESCRIPTION]
* @see      [OBJECT_RETURN_VALUE]
**/
```
> NOTE: Only use see if you are returning an object that has been created as part of the project.

### Documentation

For every document that we create please use the documentation standards.
