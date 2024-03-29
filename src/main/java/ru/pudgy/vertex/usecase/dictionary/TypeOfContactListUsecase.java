package ru.pudgy.vertex.usecase.dictionary;

import lombok.RequiredArgsConstructor;
import ru.pudgy.common.result.Result;
import ru.pudgy.vertex.model.entity.Schemata;
import ru.pudgy.vertex.model.entity.TypeOfContact;
import ru.pudgy.vertex.model.errors.VertexError;
import ru.pudgy.vertex.model.repository.TypeOfContactRepository;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@RequiredArgsConstructor
public class TypeOfContactListUsecase {
    private final TypeOfContactRepository typeOfContactRepository;

    public List<TypeOfContact> execute(Schemata schema) {
        List<TypeOfContact> result = new ArrayList<>();
        typeOfContactRepository.findAll().forEach(result::add);
        return result;
    }

    public Result<List<TypeOfContact>, VertexError> execute() {
        List<TypeOfContact> result = new ArrayList<>();
        typeOfContactRepository.findAll().forEach(result::add);
        return Result.ok(result);
    }

}
