package org.zella.db;

/**
 * Доступ к вопросам и ответам из базы
 *
 * @author zella.
 */
public interface IAnswersDao {


    /**
     * Найти ответ
     *
     * @param question
     * @return
     */
    String findAnswer(String question);
}
