package ru.pudgy.vertex.usecase.fragment.person;

import lombok.RequiredArgsConstructor;
import ru.pudgy.vertex.model.entity.FragmentPersonKey;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.repository.FragmentPersonRepository;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class FragmentPersonDeleteUsecase {
    private final FragmentPersonRepository fragmentPersonRepository;

    // TODO sure to use schema to prevent delete alien fragmentperson
    public Number execute(Schemata schema, UUID doc, UUID fragment, UUID person) {
        FragmentPersonKey key = new FragmentPersonKey(fragment, person);
        fragmentPersonRepository.deleteById(key);
        return 1;
    }
}
