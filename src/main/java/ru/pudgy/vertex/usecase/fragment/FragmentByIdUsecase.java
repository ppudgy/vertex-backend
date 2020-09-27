package ru.pudgy.vertex.usecase.fragment;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.exceptions.NotFoundException;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentByIdUsecase {
    private final FragmentRepository fragmentRepository;

    public Fragment execute(Schemata schema, UUID doc, UUID fragment) {
        return fragmentRepository.findBySchemataAndDocumentAndId(schema.getId(), doc, fragment)
                .orElseThrow(() -> new NotFoundException(String.format("fragment(%s) not found", fragment.toString())));
    }
}
