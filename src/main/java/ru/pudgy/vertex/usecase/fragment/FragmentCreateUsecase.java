package ru.pudgy.vertex.usecase.fragment;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.Fragment;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.mappers.FragmentUpdater;
import ru.pudgy.vertex.model.repository.FragmentRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentCreateUsecase {
    private final FragmentRepository fragmentRepository;
    private final FragmentUpdater fragmentUpdater;

    public Fragment execute(Schemata schema, UUID doc, Fragment newFragment) {
        return fragmentRepository.save(fragmentUpdater.createFragment(schema, doc, newFragment));
    }
}
