package restapispring.notesmodule.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import restapispring.notesmodule.notes.dto.create.CreateNoteRequest;
import restapispring.notesmodule.notes.dto.create.CreateNoteResponse;
import restapispring.notesmodule.notes.dto.delete.DeleteNoteResponse;
import restapispring.notesmodule.notes.dto.get.GetUserNotesResponse;
import restapispring.notesmodule.notes.dto.update.UpdateNoteRequest;
import restapispring.notesmodule.notes.dto.update.UpdateNoteResponse;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 1000;

    private final NoteRepository repository;

    public CreateNoteResponse create(CreateNoteRequest request) {
        Optional<CreateNoteResponse.Error> validationError = validateCreateFields(request);

        if (validationError.isPresent()) {
            return CreateNoteResponse.failed(validationError.get());
        }

        Note createdNote = repository.save(Note.builder()

                .title(request.getTitle())
                .content(request.getContent())
                .build());

        return CreateNoteResponse.success(createdNote.getId());
    }

    public GetUserNotesResponse get() {

        List<Note> notes = repository.findAll();

        return GetUserNotesResponse.success(notes);
    }

    public UpdateNoteResponse update(UpdateNoteRequest request) {
        Optional<Note> optionalNote = repository.findById(request.getId());

        if (optionalNote.isEmpty()) {
            return UpdateNoteResponse.failed(UpdateNoteResponse.Error.invalidNoteId);
        }

        Note note = optionalNote.get();

        Optional<UpdateNoteResponse.Error> validationError = validateUpdateFields(request);

        if (validationError.isPresent()) {
            return UpdateNoteResponse.failed(validationError.get());
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        repository.save(note);

        return UpdateNoteResponse.success(note);
    }

    public DeleteNoteResponse delete(long id) {
        Optional<Note> optionalNote = repository.findById(id);

        if (optionalNote.isEmpty()) {
            return DeleteNoteResponse.failed(DeleteNoteResponse.Error.invalidNoteId);
        }

        Note note = optionalNote.get();


        repository.delete(note);

        return DeleteNoteResponse.success();
    }

    private Optional<CreateNoteResponse.Error> validateCreateFields(CreateNoteRequest request) {
        if (Objects.isNull(request.getTitle()) || request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(CreateNoteResponse.Error.invalidTitle);
        }

        if (Objects.isNull(request.getContent()) || request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(CreateNoteResponse.Error.invalidTitle);
        }

        return Optional.empty();
    }

    private Optional<UpdateNoteResponse.Error> validateUpdateFields(UpdateNoteRequest request) {
        if (Objects.nonNull(request.getTitle()) && request.getTitle().length() > MAX_TITLE_LENGTH) {
            return Optional.of(UpdateNoteResponse.Error.invalidTitleLength);
        }

        if (Objects.nonNull(request.getContent()) && request.getContent().length() > MAX_CONTENT_LENGTH) {
            return Optional.of(UpdateNoteResponse.Error.invalidTitleLength);
        }

        return Optional.empty();
    }

}
