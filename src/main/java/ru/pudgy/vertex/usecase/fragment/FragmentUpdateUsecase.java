package ru.pudgy.vertex.usecase.fragment;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.FragmentUpdater;
import ru.pudgy.vertex.model.repository.FragmentRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentUpdateUsecase {
    private final FragmentRepository fragmentRepository;
    private final FragmentUpdater updater;

    public Fragment execute(Schemata schema, UUID doc, UUID fragment, Fragment ufragment) {
        return fragmentRepository.findBySchemataAndDocumentAndId(schema.getId(), doc, fragment)
                .map(frag -> updater.updateFragment(ufragment, frag))
                .map(frag -> fragmentRepository.update(frag))
                .orElseThrow(() -> new NotFoundException(String.format("fragment(%s) not found", fragment.toString())));
    }
}
