package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class CommandRegistry {

	private ObservableList<Command> commandStack = FXCollections
			.observableArrayList();

	private ObservableList<Command> undoStack = FXCollections
			.observableArrayList();

	public void executeCommand(Command command) {
		command.execute();
		commandStack.add(command);
	}

	public void redo() {
		if(undoStack.isEmpty())
			return;
		Command command = undoStack.get(undoStack.size() - 1);
		undoStack.remove(undoStack.size() - 1);
		command.redo();
		commandStack.add(command);
	}

	public void undo() {
		if(commandStack.isEmpty())
			return;
		Command command = commandStack.get(commandStack.size() - 1);
		commandStack.remove(commandStack.size() - 1);
		command.undo();
		undoStack.add(command);
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}
}
