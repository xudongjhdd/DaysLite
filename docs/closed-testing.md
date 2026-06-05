# Closed Testing Plan

This plan prepares DaysLite for Google Play closed testing.

## Goal

Validate that DaysLite can be installed, opened, used to create countdowns, switch languages, and preserve local data across app restarts.

## Tester List

Prepare at least 12 testers if Google Play requires closed testing for the account.

Suggested tester profile:

- Android phone users
- Mix of English and Chinese UI preference
- At least a few users on older Android versions

## Tester Instructions

Ask testers to complete:

1. Install DaysLite from the Play testing link.
2. Open the app and confirm the empty state is clear.
3. Create a countdown for a future date.
4. Create a yearly repeating countdown for a birthday or anniversary.
5. Add an optional note and choose a color.
6. Switch language in Settings.
7. Close and reopen the app.
8. Confirm countdowns and language setting are preserved.
9. Edit one countdown.
10. Delete one countdown.

## Feedback Questions

- Was the app easy to understand on first open?
- Was creating a countdown clear?
- Did the date picker behave as expected?
- Did the yearly repeat option make sense?
- Did English/Chinese switching work?
- Did anything feel visually confusing?
- Did the app crash or freeze?
- What feature would you expect next?

## Known Non-Goals For First Test

- No cloud sync
- No login
- No reminders or notifications
- No widgets
- No ads

## Pass Criteria

- App installs successfully
- App launches without crashing
- Countdown creation, editing, deletion work
- Local data remains after app restart
- Language preference remains after app restart
- Testers understand that data is stored locally
