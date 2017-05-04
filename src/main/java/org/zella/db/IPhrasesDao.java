package org.zella.db;

import java.util.Collection;

/**
 *
 * Доступ к фразам из базы
 *
 * @author zella.
 */
public interface IPhrasesDao {

    /**
     * @param type например "анекдот" ,"похвала" итд
     * @return все фразы данного типа
     */
    Collection<String> findPhrasesByType(String type);

    /**
     * @param type например "анекдот" ,"похвала" итд
     * @return случайную фразу данного типа
     */
    String randomPhraseByType(String type);

}
