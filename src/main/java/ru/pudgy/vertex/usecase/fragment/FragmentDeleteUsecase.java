package ru.pudgy.vertex.usecase.fragment;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentDeleteUsecase {
    private final FragmentRepository fragmentRepository;

    public Number execute(Schemata schema, UUID doc, UUID fragment) {
        return fragmentRepository.findBySchemataAndDocumentAndId(schema.getId(), doc, fragment)
                .map(frag -> fragmentRepository.deleteBySchemataAndDocumentAndId(schema.getId(), doc, fragment))
                .orElseThrow(() -> new NotFoundException(String.format("fragment(%s) fo doc(%s) not found", fragment.toString(), doc.toString())));
    }
}
