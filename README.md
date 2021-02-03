# Electronic Programme Guide
Project displays an Electronic Programme Guide for 12 Freeview channels. It will show data for the past 24 hours, today and the next 2 days.
It will also allow you to mark the programmes to record based on whether there is a tuner to record it on. if not then it will advise you when it is next being shown to allow you to record it then.

The project's entry point is com.sabaton.Controller.HomeController.main().



## API

All live programme data is obtained from [Bleb API]("http://bleb.org/tv/data/listings/").

A gap of at least 2 seconds must separate each file fetch.
 


## Maven Dependencies

- joda-time
- junit
- json-simple
- Apache Commons IO



## Code Standard

***VCS Dev Branches:***

Branch names must follow convention of `[TYPE]/[SPRINT-NAME]`.

*Branch Types:*

- Bugfix - Issue found during testing and development.
- Hotfix - Client has found an issue.
- Feature - New feature being added to system.

***Javadocs Comment Format:***

```java
/**
* [METHOD_DESCRIPTION]
* @param    [PARAMETER_NAME]    [PARAMETER_DESCRIPTION]
* @return                       [RETURN_VALUE_DESCRIPTION]
* @see      [OBJECT_RETURN_VALUE]
**/
```

***Pull Requests:***

```
# Changes
- Change 1
- Change 2

## Commits
- Add Commit Messages
```



## Feature Requirements

***A. Data Persistence:***
- Read from file (data does not have to be editable and can be hand populated).
- Programme should be identifiable based on its name, broadcast channel and start time.
- Must not rely on hardcoded filenames and should be flexible / extensible for varying name lengths / channel count in the data.
- Domain Background specifies that the program should be initialised to the local systems current time and display programs after that period.
- Acquire live data to file *Advanced Feature that refers to feature H and should not be completed before the fundamental features. 

***B. Now and Next Functionality:***
- Application should display the current and next programme based on the current system date/time for a selected channel or all channels. 

***C. Search:***
- User should be able to search for one or more keywords and retrieve EPG data where titles match.
- Programme data must display title, channel, broadcast time and should be displayed at the time it was / is scheduled.

***D. Data Extension:***
- Minimum of 4 days ideally the past 24 hrs and the future 72 hrs with a minimum of 12 channels *Domain Background specifies a minimum view of the programs next 7 days.
- Programme data must include title, short description, its start and end times and broadcast channel.

***E. Grid Layout:***
- Option should be provided to view a given number of channels for a given time period.
- User should be able to navigate forward and backwards on the schedule.
- Move through groups of channels.
- Deal with channels finishing before the block length.
- Utilise at least an ASCII design format to display the programme data.

***F. Schedule Recordings:***
- When a Programmes details are found, using the implemented EPG search or selected from a list if the EPG search isn’t implemented, it should be possible to mark a programme as scheduled for recording.
- The “Current Schedule” should be written to a schedules file on program exit, and read in on program start-up.
- It should be possible to view “current schedule” from the program’s interface.

***G. Schedule Validation:***
- Domain Background specifies that the user should only be able to flag the programme as record in advance of the viewing.
- The application must check whether there are enough “tuners” to record all the requested shows/episodes (builds upon feature F).
- The maximum number of concurrent “tuners” is determined by reading from a file, (dynamic number of tuners, not hard coded).
- If the current programme cannot be recorded, the application should identify alternative showings.

***H. Acquire Live Programme Data:***
- The software should attempt to acquire live EPG data from an available online service.
- Any data obtained in this way must be written to a data file as per feature A.

***I. Chosen Feature:***

- *Day Skipping:*
    - Give the user the option to display shows 24 hours ahead of current time, using a skip day option.

- *Favourite Show with Automated Recording:*
    - User navigates to a favourite tab and specifies their favourite show title separated by a hash e.g. The Big Bang Theory, Judge Judy.
    - Under settings, the user should be able to determine whether they want to automatically record all favourites or a select number of programmes.
