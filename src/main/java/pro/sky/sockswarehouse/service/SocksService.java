package pro.sky.sockswarehouse.service;

import pro.sky.sockswarehouse.model.SocksDto;

public interface SocksService {

    /**
     * Add socks in database
     *
     * @param socksDto
     */
    void addSocks(SocksDto socksDto);

    /**
     * Remove socks from the database
     *
     * @param socksDto
     */
    void removeSocks(SocksDto socksDto);

    /**
     * Return count of socks with request parameters and conditions
     * @param color
     * @param operation
     * @param cottonPart
     */
    Integer getAmountOfSocks(String color, String operation, Integer cottonPart);
}
