package seedu.address.logic.commands.contacts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
// import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToPersonListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code AssignedCommand}.
 */
public class AssignedCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    // TODO: Change such that expectedModel is updated with filtered tasks
    // @Test
    // public void execute_validIndexUnfilteredList_success() {
    //     Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());

    //     assertExecutionSuccess(INDEX_FIRST_PERSON);
    //     assertExecutionSuccess(INDEX_THIRD_PERSON);
    //     assertExecutionSuccess(lastPersonIndex);
    // }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    // TODO: Change such that expectedModel is updated with filtered tasks
    // @Test
    // public void execute_validIndexFilteredList_success() {
    //     showPersonAtIndex(model, INDEX_FIRST_PERSON);
    //     showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

    //     assertExecutionSuccess(INDEX_FIRST_PERSON);
    // }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        AssignedCommand selectFirstCommand = new AssignedCommand(INDEX_FIRST_PERSON);
        AssignedCommand selectSecondCommand = new AssignedCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        AssignedCommand selectFirstCommandCopy = new AssignedCommand(INDEX_FIRST_PERSON);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code AssignedCommand} with the given {@code index},
     * and checks that {@code JumpToPersonListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        AssignedCommand assignedCommand = new AssignedCommand(index);
        String expectedMessage = String.format(AssignedCommand.MESSAGE_SELECT_PERSON_SUCCESS, index.getOneBased());

        assertCommandSuccess(assignedCommand, model, commandHistory, expectedMessage, expectedModel);

        JumpToPersonListRequestEvent lastEvent = (JumpToPersonListRequestEvent) eventsCollectorRule.eventsCollector
                .getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code AssignedCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        AssignedCommand assignedCommand = new AssignedCommand(index);
        assertCommandFailure(assignedCommand, model, commandHistory, expectedMessage);
        assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
    }
}
