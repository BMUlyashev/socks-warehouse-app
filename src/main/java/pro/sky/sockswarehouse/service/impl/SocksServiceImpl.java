package pro.sky.sockswarehouse.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.sockswarehouse.exception.SocksException;
import pro.sky.sockswarehouse.mapper.SocksMapper;
import pro.sky.sockswarehouse.model.Operations;
import pro.sky.sockswarehouse.model.SocksDto;
import pro.sky.sockswarehouse.model.SocksEntity;
import pro.sky.sockswarehouse.repository.SocksRepository;
import pro.sky.sockswarehouse.service.SocksService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final SocksMapper mapper;

    @Override
    public void addSocks(SocksDto socksDto) {

        findSocks(socksDto).ifPresentOrElse(
                socks -> {
                    socks.setQuantity(socks.getQuantity() + socksDto.getQuantity());
                    socksRepository.save(socks);
                },
                () -> socksRepository.save(mapper.toEntity(socksDto))
        );
    }

    @Override
    public void removeSocks(SocksDto socksDto) {
        findSocks(socksDto).ifPresentOrElse(
                socks -> {
                    if (socks.getQuantity() >= socksDto.getQuantity()) {
                        socks.setQuantity(socks.getQuantity() - socksDto.getQuantity());
                        socksRepository.save(socks);
                    } else {
                        throw new SocksException("Socks on warehouse not enough");
                    }
                },
                () -> {
                    throw new SocksException("Socks not found");
                }
        );
    }

    private Optional<SocksEntity> findSocks(SocksDto socksDto) {
        return socksRepository
                .findByColorIgnoreCaseAndCottonPart(socksDto.getColor(), socksDto.getCottonPart());
    }

    @Override
    public Integer getAmountOfSocks(String color, String operation, Integer cottonPart) {
        Operations operations = Operations.getNameOperation(operation);
        String lowColor = color.toLowerCase();
        Optional<Integer> result;
        switch (operations) {
            case LESS_THEN:
                result = socksRepository.getAmountSocksCottonLessThen(lowColor, cottonPart);
                break;
            case EQUAL:
                result = socksRepository.getAmountSocksCottonEqual(lowColor, cottonPart);
                break;
            case MORE_THEN:
                result = socksRepository.getAmountSocksCottonMoreThen(lowColor, cottonPart);
                break;
            default:
                throw new SocksException("Неверный параметр {operations}");
        }
        return result.orElse(0);
    }
}
