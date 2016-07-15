import org.junit.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * 　　　　　　　 ┏┓   ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　  ┃　　　┃
 * 　　　　　　　  ┃　　　┃ + + + +
 * 　　　　　　　  ┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　  ┃　　　┃ + 　　　　神兽保佑,代码无BUG
 * 　　　　　　　  ┃　　　┃
 * 　　　　　　　  ┃　　　┃　　+
 * 　　　　　　　  ┃　 　　┗━━━┓ + +
 * 　　　　　　　  ┃ 　　　　　　　┣┓
 * 　　　　　　　  ┃ 　　　　　　　┏┛
 * 　　　　　　　  ┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　┃┫┫ ┃┫┫
 * 　　　　　　　　　┗┻┛ ┗┻┛+ + + +
 * Copyright (c) 2016 Mljia , All rights reserved.
 * http://www.mljia.cn/
 *
 * @author Paul
 * @description
 * @since 2016/7/14.
 */


public class MockTest {


    /*验证一些行为*/
    @Test
    public void behaviour() {
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.clear();
        /*验证是否添加过 one*/
        verify(mockedList).add("one");
        /*验证是否清除过数组*/
        verify(mockedList).clear();
    }


    /*立一些桩，当执行某些操作的时候给出相应的状态*/
    @Test
    public void stubbing() {
        LinkedList mockedList = mock(LinkedList.class);

        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenReturn(new RuntimeException());

        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(1));
        System.out.println(mockedList.get(999));
        verify(mockedList).get(0);
    }

    /**/
    @Test
    public void argumentMatchers() {
        LinkedList mockedList = mock(LinkedList.class);
        when(mockedList.get(anyInt())).thenReturn("element");
        //stubbing using hamcrest (let's say isValid() returns your own hamcrest matcher):
        /*when(mockedList.contains(argThat(isValid()))).thenReturn("element");*/
        System.out.println(mockedList.get(999));
        verify(mockedList).get(anyInt());
    }


    @Test
    public void numberOfInvocations() {
        LinkedList mockedList = mock(LinkedList.class);
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        /*不传递默认为一次*/
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");


        verify(mockedList, never()).add("never happened");

    }


    @Test
    public void stubbingMethodException() {
        Object o = mock(Object.class);
        doThrow(new RuntimeException()).when(o).toString();
        o.toString();
    }

    /*验证指定的调用次序*/
    @Test
    public void verificationInOrder() {
        List singleMock = mock(List.class);
        singleMock.add("was added first");
        singleMock.add("was added second");


        InOrder inOrder = inOrder(singleMock);

        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        List firstMock = mock(List.class);
        List secondMock = mock(List.class);
        List third = mock(List.class);
        firstMock.add("was called first");
        secondMock.add("was called second");


        inOrder = inOrder(firstMock, secondMock);
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");
    }

    /*验证某些方法是否被调用过*/
    @Test
    public void neverHappened() {

        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        List mockThree = mock(List.class);
        //using mocks - only mockOne is interacted
        mockOne.add("one");
        /*验证方法被调用过*/
        //ordinary verification
        verify(mockOne).add("one");

        //验证方法没有没调用过
        //verify that method was never called on a mock
        verify(mockOne, never()).add("two");
        //mockTwo.add("");
        //verify that other mocks were not interacted
        /*验证某个对象从来没有被使用过*/
        verifyZeroInteractions(mockTwo, mockThree);
    }
}

