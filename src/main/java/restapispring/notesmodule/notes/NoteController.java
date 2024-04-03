package restapispring.notesmodule.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import restapispring.notesmodule.notes.dto.create.CreateNoteRequest;
import restapispring.notesmodule.notes.dto.create.CreateNoteResponse;
import restapispring.notesmodule.notes.dto.delete.DeleteNoteResponse;
import restapispring.notesmodule.notes.dto.get.GetUserNotesResponse;
import restapispring.notesmodule.notes.dto.update.UpdateNoteRequest;
import restapispring.notesmodule.notes.dto.update.UpdateNoteResponse;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public CreateNoteResponse create(@RequestBody CreateNoteRequest request) {
        return noteService.create(request);
    }

    @GetMapping
    public GetUserNotesResponse get() {
        return noteService.get();
    }

    @PatchMapping
    public UpdateNoteResponse update(Principal principal, @RequestBody UpdateNoteRequest request) {
        return noteService.update(request);
    }

    @DeleteMapping
    public DeleteNoteResponse delete(Principal principal, @RequestParam(name = "id") long id) {
        return noteService.delete(id);
    }
}
