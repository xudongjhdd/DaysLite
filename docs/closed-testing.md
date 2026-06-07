# Closed Testing Plan

This plan prepares DaysLite for Google Play closed testing.

## Google Play Requirement

For a new personal developer account created after November 13, 2023:

- Recruit at least 12 testers with Google accounts
- Each tester must opt in to the closed test
- At least 12 testers must remain opted in continuously for 14 days
- A tester who opts out interrupts their consecutive-day count
- Keep testers engaged and retain evidence of real feedback
- Apply for production access only after the requirement is complete

Recruit more than 12 people. A target of 15-20 testers provides a buffer for people who do not opt in or who leave the test.

## Goal

Validate that DaysLite can be installed, opened, used to create countdowns, switch languages, and preserve local data across app restarts.

## Tester List

Store real tester details locally. Do not commit names or email addresses to this public repository.

Suggested tester profile:

- Android phone users
- Mix of English and Chinese UI preference
- At least a few users on older Android versions
- Mix of phone brands and screen sizes

Google Play tester emails must belong to Google Accounts or Google Workspace accounts.

Use:

```text
docs/testing/tester-roster-template.csv
```

When Play Console is available, create a separate upload CSV with one tester email address per line and no header. The file must be UTF-8 without BOM.

## Test Timeline

Day 0:

- Upload the release AAB to the closed testing track
- Add testers by email list or Google Group
- Publish the closed test
- Send the opt-in link and instructions

Days 1-2:

- Confirm at least 12 testers have opted in
- Ask each tester to install and launch the app
- Replace non-responsive invitees while there is still time

Days 3-7:

- Ask testers to complete all core scenarios
- Gather device, Android version, usability, and bug feedback
- Record issues and fixes

Days 8-13:

- Confirm testers remain opted in
- Follow up on incomplete scenarios
- Verify any test release updates

Day 14 or later:

- Confirm at least 12 testers have remained opted in continuously
- Summarize engagement and feedback
- Complete the production-access answers
- Apply for production access

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

Feedback templates:

```text
docs/testing/feedback-form.md
docs/testing/test-results-template.csv
```

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

## Production Access Evidence

Keep these records:

- Test start date and end date
- Tester opt-in confirmation
- Number of active testers
- Device and Android-version coverage
- Completed test scenarios
- Feedback themes
- Bugs found and fixes made
- Final production-readiness decision

Use:

```text
docs/testing/production-access-answers.md
```

Do not submit invented feedback. Replace every placeholder with evidence collected during the real closed test.

## Official References

- Testing requirement:
  `https://support.google.com/googleplay/android-developer/answer/14151465`
- Set up a closed test:
  `https://support.google.com/googleplay/android-developer/answer/9845334`
- Pre-launch report:
  `https://support.google.com/googleplay/android-developer/answer/9842757`
