package com.king.caesar.gamma.rpc.codec.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.Map;

import com.king.caesar.gamma.core.util.ClassUtils;
import com.king.caesar.gamma.rpc.api.buffer.BufferWrapper;
import com.king.caesar.gamma.rpc.api.codec.serializer.Serializer;
import com.king.caesar.gamma.rpc.codec.ObjectType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * 包装Netty提供的ByteBuf，提供更丰富的类型支持。
 * 
 * @author: Caesar
 * @date: 2017年5月30日 下午12:09:58
 */
public class NettyBufferWrapper extends BufferWrapper
{
    public static final int DEFAULT_CAPACITY = 256;
    
    private ByteBuf buffer;
    
    public NettyBufferWrapper(ByteBuf buffer)
    {
        this.buffer = buffer;
    }
    
    // 默认开辟256个字节的内存，这里的内存是direct buffer，由于涉及到网络IO，可以利用zero-copy的技术来提高性能。
    public static NettyBufferWrapper allocate()
    {
        return new NettyBufferWrapper(PooledByteBufAllocator.DEFAULT.directBuffer(DEFAULT_CAPACITY));
    }
    
    public static NettyBufferWrapper allocate(int bufferSize)
    {
        return new NettyBufferWrapper(PooledByteBufAllocator.DEFAULT.directBuffer(bufferSize));
    }
    
    @Override
    public int refCnt()
    {
        return buffer.refCnt();
    }
    
    @Override
    public boolean release()
    {
        return buffer.release();
    }
    
    @Override
    public boolean release(int decrement)
    {
        return buffer.release(decrement);
    }
    
    @Override
    public int capacity()
    {
        return buffer.capacity();
    }
    
    @Override
    public ByteBuf capacity(int newCapacity)
    {
        return buffer.capacity(newCapacity);
    }
    
    @Override
    public int maxCapacity()
    {
        return buffer.maxCapacity();
    }
    
    @Override
    public ByteBufAllocator alloc()
    {
        return buffer.alloc();
    }
    
    @Override
    public ByteOrder order()
    {
        return buffer.order();
    }
    
    @Override
    public ByteBuf order(ByteOrder endianness)
    {
        return buffer.order(endianness);
    }
    
    @Override
    public ByteBuf unwrap()
    {
        return buffer.unwrap();
    }
    
    @Override
    public boolean isDirect()
    {
        return buffer.isDirect();
    }
    
    @Override
    public int readerIndex()
    {
        return buffer.readerIndex();
    }
    
    @Override
    public ByteBuf readerIndex(int readerIndex)
    {
        return buffer.readerIndex(readerIndex);
    }
    
    @Override
    public int writerIndex()
    {
        return buffer.writerIndex();
    }
    
    @Override
    public ByteBuf writerIndex(int writerIndex)
    {
        return buffer.writerIndex(writerIndex);
    }
    
    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex)
    {
        return buffer.setIndex(readerIndex, writerIndex);
    }
    
    @Override
    public int readableBytes()
    {
        return buffer.readableBytes();
    }
    
    @Override
    public int writableBytes()
    {
        return buffer.writableBytes();
    }
    
    @Override
    public int maxWritableBytes()
    {
        return buffer.maxWritableBytes();
    }
    
    @Override
    public boolean isReadable()
    {
        return buffer.isReadable();
    }
    
    @Override
    public boolean isReadable(int size)
    {
        return buffer.isReadable(size);
    }
    
    @Override
    public boolean isWritable()
    {
        return buffer.isWritable();
    }
    
    @Override
    public boolean isWritable(int size)
    {
        return buffer.isWritable(size);
    }
    
    @Override
    public ByteBuf clear()
    {
        return buffer.clear();
    }
    
    @Override
    public ByteBuf markReaderIndex()
    {
        return buffer.markReaderIndex();
    }
    
    @Override
    public ByteBuf resetReaderIndex()
    {
        return buffer.resetReaderIndex();
    }
    
    @Override
    public ByteBuf markWriterIndex()
    {
        return buffer.markWriterIndex();
    }
    
    @Override
    public ByteBuf resetWriterIndex()
    {
        return buffer.resetWriterIndex();
    }
    
    @Override
    public ByteBuf discardReadBytes()
    {
        return buffer.discardReadBytes();
    }
    
    @Override
    public ByteBuf discardSomeReadBytes()
    {
        return buffer.discardSomeReadBytes();
    }
    
    @Override
    public ByteBuf ensureWritable(int minWritableBytes)
    {
        return buffer.ensureWritable(minWritableBytes);
    }
    
    @Override
    public int ensureWritable(int minWritableBytes, boolean force)
    {
        return buffer.ensureWritable(minWritableBytes, force);
    }
    
    @Override
    public boolean getBoolean(int index)
    {
        return buffer.getBoolean(index);
    }
    
    @Override
    public byte getByte(int index)
    {
        return buffer.getByte(index);
    }
    
    @Override
    public short getUnsignedByte(int index)
    {
        return buffer.getUnsignedByte(index);
    }
    
    @Override
    public short getShort(int index)
    {
        return buffer.getShort(index);
    }
    
    @Override
    public int getUnsignedShort(int index)
    {
        return buffer.getUnsignedShort(index);
    }
    
    @Override
    public int getMedium(int index)
    {
        return buffer.getMedium(index);
    }
    
    @Override
    public int getUnsignedMedium(int index)
    {
        return buffer.getUnsignedMedium(index);
    }
    
    @Override
    public int getInt(int index)
    {
        return buffer.getInt(index);
    }
    
    @Override
    public long getUnsignedInt(int index)
    {
        return buffer.getUnsignedInt(index);
    }
    
    @Override
    public long getLong(int index)
    {
        return buffer.getLong(index);
    }
    
    @Override
    public char getChar(int index)
    {
        return buffer.getChar(index);
    }
    
    @Override
    public float getFloat(int index)
    {
        return buffer.getFloat(index);
    }
    
    @Override
    public double getDouble(int index)
    {
        return buffer.getDouble(index);
    }
    
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst)
    {
        return buffer.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length)
    {
        return buffer.getBytes(index, dst, length);
    }
    
    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length)
    {
        return buffer.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(int index, byte[] dst)
    {
        return buffer.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length)
    {
        return buffer.getBytes(index, dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst)
    {
        return buffer.getBytes(index, dst);
    }
    
    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length)
        throws IOException
    {
        return buffer.getBytes(index, out, length);
    }
    
    @Override
    public int getBytes(int index, GatheringByteChannel out, int length)
        throws IOException
    {
        return buffer.getBytes(index, out, length);
    }
    
    @Override
    public ByteBuf setBoolean(int index, boolean value)
    {
        return buffer.setBoolean(index, value);
    }
    
    @Override
    public ByteBuf setByte(int index, int value)
    {
        return buffer.setByte(index, value);
    }
    
    @Override
    public ByteBuf setShort(int index, int value)
    {
        return buffer.setShort(index, value);
    }
    
    @Override
    public ByteBuf setMedium(int index, int value)
    {
        return buffer.setMedium(index, value);
    }
    
    @Override
    public ByteBuf setInt(int index, int value)
    {
        return buffer.setInt(index, value);
    }
    
    @Override
    public ByteBuf setLong(int index, long value)
    {
        return buffer.setLong(index, value);
    }
    
    @Override
    public ByteBuf setChar(int index, int value)
    {
        return buffer.setChar(index, value);
    }
    
    @Override
    public ByteBuf setFloat(int index, float value)
    {
        return buffer.setFloat(index, value);
    }
    
    @Override
    public ByteBuf setDouble(int index, double value)
    {
        return buffer.setDouble(index, value);
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuf src)
    {
        return buffer.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length)
    {
        return buffer.setBytes(index, src, length);
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length)
    {
        return buffer.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(int index, byte[] src)
    {
        return buffer.setBytes(index, src);
    }
    
    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length)
    {
        return buffer.setBytes(index, src, srcIndex, length);
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuffer src)
    {
        return buffer.setBytes(index, src);
    }
    
    @Override
    public int setBytes(int index, InputStream in, int length)
        throws IOException
    {
        return buffer.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length)
        throws IOException
    {
        return buffer.setBytes(index, in, length);
    }
    
    @Override
    public ByteBuf setZero(int index, int length)
    {
        return buffer.setZero(index, length);
    }
    
    @Override
    public boolean readBoolean()
    {
        return buffer.readBoolean();
    }
    
    @Override
    public byte readByte()
    {
        return buffer.readByte();
    }
    
    @Override
    public short readUnsignedByte()
    {
        return buffer.readUnsignedByte();
    }
    
    @Override
    public short readShort()
    {
        return buffer.readShort();
    }
    
    @Override
    public int readUnsignedShort()
    {
        return buffer.readUnsignedShort();
    }
    
    @Override
    public int readMedium()
    {
        return buffer.readMedium();
    }
    
    @Override
    public int readUnsignedMedium()
    {
        return buffer.readUnsignedMedium();
    }
    
    @Override
    public int readInt()
    {
        return buffer.readInt();
    }
    
    @Override
    public long readUnsignedInt()
    {
        return buffer.readUnsignedInt();
    }
    
    @Override
    public long readLong()
    {
        return buffer.readLong();
    }
    
    @Override
    public char readChar()
    {
        return buffer.readChar();
    }
    
    @Override
    public float readFloat()
    {
        return buffer.readFloat();
    }
    
    @Override
    public double readDouble()
    {
        return buffer.readDouble();
    }
    
    @Override
    public ByteBuf readBytes(int length)
    {
        return buffer.readBytes(length);
    }
    
    @Override
    public ByteBuf readSlice(int length)
    {
        return buffer.readSlice(length);
    }
    
    @Override
    public ByteBuf readBytes(ByteBuf dst)
    {
        return buffer.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(ByteBuf dst, int length)
    {
        return buffer.readBytes(dst, length);
    }
    
    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length)
    {
        return buffer.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(byte[] dst)
    {
        return buffer.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length)
    {
        return buffer.readBytes(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(ByteBuffer dst)
    {
        return buffer.readBytes(dst);
    }
    
    @Override
    public ByteBuf readBytes(OutputStream out, int length)
        throws IOException
    {
        return buffer.readBytes(out, length);
    }
    
    @Override
    public int readBytes(GatheringByteChannel out, int length)
        throws IOException
    {
        return buffer.readBytes(out, length);
    }
    
    @Override
    public ByteBuf skipBytes(int length)
    {
        return buffer.skipBytes(length);
    }
    
    @Override
    public ByteBuf writeBoolean(boolean value)
    {
        return buffer.writeBoolean(value);
    }
    
    @Override
    public ByteBuf writeByte(int value)
    {
        return buffer.writeByte(value);
    }
    
    @Override
    public ByteBuf writeShort(int value)
    {
        return buffer.writeShort(value);
    }
    
    @Override
    public ByteBuf writeMedium(int value)
    {
        return buffer.writeMedium(value);
    }
    
    @Override
    public ByteBuf writeInt(int value)
    {
        return buffer.writeInt(value);
    }
    
    @Override
    public ByteBuf writeLong(long value)
    {
        return buffer.writeLong(value);
    }
    
    @Override
    public ByteBuf writeChar(int value)
    {
        return buffer.writeChar(value);
    }
    
    @Override
    public ByteBuf writeFloat(float value)
    {
        return buffer.writeFloat(value);
    }
    
    @Override
    public ByteBuf writeDouble(double value)
    {
        return buffer.writeDouble(value);
    }
    
    @Override
    public ByteBuf writeBytes(ByteBuf src)
    {
        return buffer.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(ByteBuf src, int length)
    {
        return buffer.writeBytes(src, length);
    }
    
    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length)
    {
        return buffer.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(byte[] src)
    {
        return buffer.writeBytes(src);
    }
    
    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length)
    {
        return buffer.writeBytes(src, srcIndex, length);
    }
    
    @Override
    public ByteBuf writeBytes(ByteBuffer src)
    {
        return buffer.writeBytes(src);
    }
    
    @Override
    public int writeBytes(InputStream in, int length)
        throws IOException
    {
        return buffer.writeBytes(in, length);
    }
    
    @Override
    public int writeBytes(ScatteringByteChannel in, int length)
        throws IOException
    {
        return buffer.writeBytes(in, length);
    }
    
    @Override
    public ByteBuf writeZero(int length)
    {
        return buffer.writeZero(length);
    }
    
    @Override
    public int indexOf(int fromIndex, int toIndex, byte value)
    {
        return buffer.indexOf(fromIndex, toIndex, value);
    }
    
    @Override
    public int bytesBefore(byte value)
    {
        return buffer.bytesBefore(value);
    }
    
    @Override
    public int bytesBefore(int length, byte value)
    {
        return buffer.bytesBefore(length, value);
    }
    
    @Override
    public int bytesBefore(int index, int length, byte value)
    {
        return buffer.bytesBefore(index, length, value);
    }
    
    @Override
    public int forEachByte(ByteBufProcessor processor)
    {
        return buffer.forEachByte(processor);
    }
    
    @Override
    public int forEachByte(int index, int length, ByteBufProcessor processor)
    {
        return buffer.forEachByte(index, length, processor);
    }
    
    @Override
    public int forEachByteDesc(ByteBufProcessor processor)
    {
        return buffer.forEachByteDesc(processor);
    }
    
    @Override
    public int forEachByteDesc(int index, int length, ByteBufProcessor processor)
    {
        return buffer.forEachByteDesc(index, length, processor);
    }
    
    @Override
    public ByteBuf copy()
    {
        return buffer.copy();
    }
    
    @Override
    public ByteBuf copy(int index, int length)
    {
        return buffer.copy(index, length);
    }
    
    @Override
    public ByteBuf slice()
    {
        return buffer.slice();
    }
    
    @Override
    public ByteBuf slice(int index, int length)
    {
        return buffer.slice(index, length);
    }
    
    @Override
    public ByteBuf duplicate()
    {
        return buffer.duplicate();
    }
    
    @Override
    public int nioBufferCount()
    {
        return buffer.nioBufferCount();
    }
    
    @Override
    public ByteBuffer nioBuffer()
    {
        return buffer.nioBuffer();
    }
    
    @Override
    public ByteBuffer nioBuffer(int index, int length)
    {
        return buffer.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer internalNioBuffer(int index, int length)
    {
        return buffer.internalNioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers()
    {
        return buffer.nioBuffers();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(int index, int length)
    {
        return buffer.nioBuffers(index, length);
    }
    
    @Override
    public boolean hasArray()
    {
        return buffer.hasArray();
    }
    
    @Override
    public byte[] array()
    {
        return buffer.array();
    }
    
    @Override
    public int arrayOffset()
    {
        return buffer.arrayOffset();
    }
    
    @Override
    public boolean hasMemoryAddress()
    {
        return buffer.hasMemoryAddress();
    }
    
    @Override
    public long memoryAddress()
    {
        return buffer.memoryAddress();
    }
    
    @Override
    public String toString(Charset charset)
    {
        return buffer.toString(charset);
    }
    
    @Override
    public String toString(int index, int length, Charset charset)
    {
        return buffer.toString(index, length, charset);
    }
    
    @Override
    public int hashCode()
    {
        return buffer.hashCode();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return buffer.equals(obj);
    }
    
    @Override
    public int compareTo(ByteBuf buffer)
    {
        return buffer.compareTo(buffer);
    }
    
    @Override
    public String toString()
    {
        return buffer.toString();
    }
    
    @Override
    public ByteBuf retain(int increment)
    {
        return buffer.retain(increment);
    }
    
    @Override
    public ByteBuf retain()
    {
        return buffer.retain();
    }
    
    /**
     * 写入String
     * 
     * @param data
     * @return
     */
    public ByteBuf writeString(String data)
    {
        return writeString(data, Charset.forName("UTF-8"));
    }
    
    /**
     * 写入String
     * 
     * @param data
     * @return
     */
    public ByteBuf writeString(String data, Charset charset)
    {
        if (null == data || data.length() == 0)
        {
            return buffer.writeInt(-1);
        }
        byte[] serviceBytes = data.getBytes(charset);
        buffer.writeInt(serviceBytes.length);
        return buffer.writeBytes(serviceBytes);
    }
    
    @Override
    public String readString()
    {
        return readString(Charset.forName("UTF-8"));
    }
    
    @Override
    public String readString(Charset charset)
    {
        int charLength = buffer.readInt();
        if (charLength == -1)
        {
            return null;
        }
        byte[] bytes = new byte[charLength];
        buffer.readBytes(bytes);
        return new String(bytes, charset);
    }
    
    private String readString(int size)
    {
        return readString(size, Charset.forName("UTF-8"));
    }
    
    /**
     * 直接根据字符串长度读取
     * 
     * @param size
     * @param charset
     * @return
     */
    private String readString(int size, Charset charset)
    {
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return new String(bytes, charset);
    }
    
    /**
     * 写单个对象，判断类型，如果是基础类型，直接写入，如果是复合类型，先写入类名，最后写入body。 区别与writeObjectDirect方法
     */
    @Override
    public ByteBuf writeObject(Object data, Serializer serializer)
    {
        // 空对象为-1
        if (null == data || null == serializer)
        {
            return buffer.writeInt(-1);
        }
        if (data instanceof String)
        {
            buffer.writeByte(ObjectType.STRING);
            writeString((String)data);
        }
        else if (data instanceof Long)
        {
            buffer.writeByte(ObjectType.LONG);
            buffer.writeLong((Long)data);
        }
        else if (data instanceof Short)
        {
            buffer.writeByte(ObjectType.SHORT);
            buffer.writeShort((Short)data);
        }
        else if (data instanceof Integer)
        {
            buffer.writeByte(ObjectType.INTEGER);
            buffer.writeInt((Integer)data);
        }
        else if (data instanceof Byte)
        {
            buffer.writeByte(ObjectType.BYTE);
            buffer.writeByte((Byte)data);
        }
        else if (data instanceof Boolean)
        {
            buffer.writeByte(ObjectType.BOOLEAN);
            buffer.writeBoolean((Boolean)data);
        }
        else if (data instanceof Double)
        {
            buffer.writeByte(ObjectType.DOUBLE);
            buffer.writeDouble((Double)data);
        }
        else if (data instanceof Float)
        {
            buffer.writeByte(ObjectType.FLOAT);
            buffer.writeFloat((Float)data);
        }
        else if (data instanceof Character)
        {
            buffer.writeByte(ObjectType.CHAR);
            buffer.writeChar((Character)data);
        }
        else
        {
            buffer.writeByte(ObjectType.OBJECT);
            writeObjectDirect(data, serializer);
        }
        return this;
    }
    
    /**
     * 写对象数组
     */
    @Override
    public ByteBuf writeObjects(Object[] datas, Serializer serializer)
    {
        // 空对象为-1
        if (null == datas || null == serializer)
        {
            return buffer.writeInt(-1);
        }
        // 先写入数组长度
        buffer.writeInt(datas.length);
        for (Object data : datas)
        {
            // 循环写入数组内的对象
            writeObject(data, serializer);
        }
        return buffer;
    }
    
    @Override
    public Object readObject(Serializer serializer)
    {
        int objectType = buffer.readInt();
        if (-1 == objectType)
        {
            return null;
        }
        switch (objectType)
        {
            case ObjectType.STRING:
                return readString();
            case ObjectType.LONG:
                return readLong();
            case ObjectType.SHORT:
                return readShort();
            case ObjectType.INTEGER:
                return readInt();
            case ObjectType.BYTE:
                return readByte();
            case ObjectType.BOOLEAN:
                return readBoolean();
            case ObjectType.DOUBLE:
                return readDouble();
            case ObjectType.FLOAT:
                return readFloat();
            case ObjectType.CHAR:
                return readChar();
            default:
                return readObjectDirect(serializer);
        }
    }
    
    @Override
    public Object[] readObjects(Serializer serializer)
    {
        int arraySize = buffer.readInt();
        if (-1 == arraySize)
        {
            return null;
        }
        Object[] array = new Object[arraySize];
        for (int i = 0; i < arraySize; i++)
        {
            array[i] = readObject(serializer);
        }
        return array;
    }
    
    /**
     * 对应请求参数或者响应直接写入避免冗余的判断 <b>
     * <p>
     * 1、写入对象名。
     * <p>
     * 2、写入对象数据
     */
    @Override
    public ByteBuf writeObjectDirect(Object data, Serializer serializer)
    {
        // 空对象为-1
        if (null == data || null == serializer)
        {
            return buffer.writeInt(-1);
        }
        // 写入对象名
        writeString(data.getClass().getName());
        byte[] bytes = serializer.serialize(data);
        buffer.writeInt(bytes.length);
        return buffer.writeBytes(bytes);
    }
    
    @Override
    public ByteBuf writeObjectsDirect(Object[] datas, Serializer serializer)
    {
        // 空对象为-1
        if (null == datas || null == serializer)
        {
            return buffer.writeInt(-1);
        }
        // 先写入数组长度
        buffer.writeInt(datas.length);
        for (Object data : datas)
        {
            // 循环写入数组内的对象
            writeObjectDirect(data, serializer);
        }
        return buffer;
    }
    
    /**
     * 直接读取类名
     */
    @Override
    public Object readObjectDirect(Serializer serializer)
    {
        int classNameSize = buffer.readInt();
        if (-1 == classNameSize)
        {
            return null;
        }
        
        String className = readString(classNameSize);
        Class<?> clazz = ClassUtils.forName(className, null);
        int objectSize = buffer.readInt();
        byte[] bytes = new byte[objectSize];
        buffer.readBytes(bytes);
        return serializer.deserialize(bytes, clazz);
    }
    
    @Override
    public Object[] readObjectsDirect(Serializer serializer)
    {
        int arraySize = buffer.readInt();
        if (-1 == arraySize)
        {
            return null;
        }
        Object[] array = new Object[arraySize];
        for (int i = 0; i < arraySize; i++)
        {
            array[i] = readObjectDirect(serializer);
        }
        return array;
    }
    
    @Override
    public <V> ByteBuf writeMap(Map<String, V> map, Class<V> valueClazz, Serializer serializer)
    {
        if (null == map)
        {
            buffer.writeInt(-1);
        }
        byte[] bytes = serializer.serializeMap(map, valueClazz);
        buffer.writeInt(bytes.length);
        return buffer.writeBytes(bytes);
    }
    
    @Override
    public <V> Map<String, V> readMap(Class<V> valueClazz, Serializer serializer)
    {
        int byteSize = buffer.readInt();
        if (-1 == byteSize)
        {
            return null;
        }
        byte[] bytes = new byte[byteSize];
        buffer.readBytes(bytes);
        return serializer.deserializeMap(bytes, valueClazz);
    }
}
