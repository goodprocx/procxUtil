//==============================================================================
// Created on 2006-10-12
// Project_name: icrm Copyright By Panther. 
//==============================================================================
/**
 * This file is part of the CQ_CRM project and it is distributed under the 
 * terms of the ZTE Workgroup.
 * 
 * The ZTE License
 * Copyright (c) 2006 Collaborative Development Group for Interface 
 *                    
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the "Software"), to deal in the Software 
 * without restriction, including without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.procx.util;

/**
 * <p>用于填充字符生成字符串
 * </p>
 * 
 * <p>Copyright: Copyright (c) 2005-2006 ZTE.com</p>
 * 
 * @author Pan.xuyang
 * @version 1.3, $Revision: 1.1 $, 2006-10-12
 */

public class CharFillUtils {
    
    /**
     * @description: 生成length长度的由character组成的字符串
     * @param character 需要填充的字符
     * @param length 需要填充的长度
     * @return
     */
    public static String fill(char character, int length ){
        StringBuffer sb = new StringBuffer(length);
        
        for(int i = 0; i < length; i++){
            sb.append(character);
        }
        return sb.toString();
    }
}
