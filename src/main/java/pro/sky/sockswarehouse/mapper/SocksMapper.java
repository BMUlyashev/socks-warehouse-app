package pro.sky.sockswarehouse.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pro.sky.sockswarehouse.model.SocksDto;
import pro.sky.sockswarehouse.model.SocksEntity;

@Component
public class SocksMapper {
    private final ModelMapper mapper;
    private final Converter<String, String> setLowerCase = src -> src.getSource().toLowerCase();

    public SocksMapper() {
        mapper = new ModelMapper();
        mapper.createTypeMap(SocksDto.class, SocksEntity.class)
                .addMappings(m -> m.using(setLowerCase).map(SocksDto::getColor, SocksEntity::setColor));
    }

    public SocksEntity toEntity(SocksDto socksDto) {
        return mapper.map(socksDto, SocksEntity.class);
    }
}
