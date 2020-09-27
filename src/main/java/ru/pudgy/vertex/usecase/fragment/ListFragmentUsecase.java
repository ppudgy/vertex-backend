package ru.pudgy.vertex.usecase.fragment;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentRepository;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class ListFragmentUsecase {
    private final FragmentRepository fragmentRepository;

    public List<Fragment> execute(Schemata schema, UUID doc) {
        return fragmentRepository.findBySchemataAndDocument(schema.getId(), doc);
    }
}
