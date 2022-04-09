package account.util;

import account.dto.DtoMarker;
import account.entity.EntityMarker;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MappingUtils {

    ModelMapper mapper;

    {
        mapper = new ModelMapper();
    }

    public <T extends DtoMarker, U extends EntityMarker> U convertDtoToEntity(T dto, Class<U> uClass) {
        return mapper.map(dto, uClass);
    }

    public  <T extends EntityMarker, U extends DtoMarker> U convertEntityToDto(T entity, Class<U> uClass) {
        return mapper.map(entity, uClass);
    }
}
