package pro.sky.sockswarehouse.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.sockswarehouse.exception.SocksException;
import pro.sky.sockswarehouse.mapper.SocksMapper;
import pro.sky.sockswarehouse.model.SocksDto;
import pro.sky.sockswarehouse.model.SocksEntity;
import pro.sky.sockswarehouse.repository.SocksRepository;
import pro.sky.sockswarehouse.service.impl.SocksServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {

    @InjectMocks
    SocksServiceImpl socksService;
    @Mock
    SocksRepository socksRepository;

    @Spy
    SocksMapper mapper;

    @Test
    void addSocksWhenNotExistInDb() {
        SocksDto socksDto = new SocksDto("red", 50, 1);
        when(socksRepository.findByColorIgnoreCaseAndCottonPart(any(String.class), any(Integer.class)))
                .thenReturn(Optional.empty());
        socksService.addSocks(socksDto);
        verify(socksRepository, times(1)).save(any(SocksEntity.class));
    }

    @Test
    void addSocksWhenExistInDb() {
        SocksDto socksDto = new SocksDto("red", 50, 1);
        SocksEntity socks = new SocksEntity(1, "red", 50, 1);
        when(socksRepository.findByColorIgnoreCaseAndCottonPart(any(String.class), any(Integer.class)))
                .thenReturn(Optional.of(socks));
        socksService.addSocks(socksDto);
        verify(socksRepository, times(1)).save(any(SocksEntity.class));
    }


    @Test
    void removeSocksThenExistsAndEnough() {
        SocksDto socksDto = new SocksDto("red", 50, 1);
        SocksEntity socks = new SocksEntity(1, "red", 50, 10);
        when(socksRepository.findByColorIgnoreCaseAndCottonPart(any(String.class), any(Integer.class)))
                .thenReturn(Optional.of(socks));
        socksService.removeSocks(socksDto);
        verify(socksRepository, times(1)).save(any(SocksEntity.class));
    }

    @Test
    void removeSocksThenExistsAndNotEnough() {
        SocksDto socksDto = new SocksDto("red", 50, 10);
        SocksEntity socks = new SocksEntity(1, "red", 50, 5);
        when(socksRepository.findByColorIgnoreCaseAndCottonPart(any(String.class), any(Integer.class)))
                .thenReturn(Optional.of(socks));
        assertThatThrownBy(() -> socksService.removeSocks(socksDto))
                .isInstanceOf(SocksException.class)
                .hasMessage("Socks on warehouse not enough");
    }

    @Test
    void removeSocksThenNotExists() {
        SocksDto socksDto = new SocksDto("red", 50, 10);
        when(socksRepository.findByColorIgnoreCaseAndCottonPart(any(String.class), any(Integer.class)))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> socksService.removeSocks(socksDto))
                .isInstanceOf(SocksException.class)
                .hasMessage("Socks not found");
    }

    @Test
    void getAmountOfSocksWhenOperationIsLessThen() {
        when(socksRepository.getAmountSocksCottonLessThen("red", 50))
                .thenReturn(Optional.of(10));
        assertThat(socksService.getAmountOfSocks("red", "lessThan", 50))
                .isEqualTo(10);
    }

    @Test
    void getAmountOfSocksWhenOperationIsEqual() {
        when(socksRepository.getAmountSocksCottonEqual("red", 50))
                .thenReturn(Optional.of(10));
        assertThat(socksService.getAmountOfSocks("red", "equal", 50))
                .isEqualTo(10);
    }

    @Test
    void getAmountOfSocksWhenOperationIsMoreThen() {
        when(socksRepository.getAmountSocksCottonMoreThen("red", 50))
                .thenReturn(Optional.of(10));
        assertThat(socksService.getAmountOfSocks("red", "moreThan", 50))
                .isEqualTo(10);
    }

    @Test
    void getAmountOfSocksWhenOperationIsLessThenNotExists() {
        when(socksRepository.getAmountSocksCottonLessThen("red", 50))
                .thenReturn(Optional.empty());
        assertThat(socksService.getAmountOfSocks("red", "lessThan", 50))
                .isEqualTo(0);
    }

    @Test
    void getAmountOfSocksWhenBadOperation() {
        assertThatThrownBy(() -> socksService.getAmountOfSocks("red", "unknown", 50))
                .isInstanceOf(SocksException.class)
                .hasMessage("Неверный параметр {operations}");
    }
}