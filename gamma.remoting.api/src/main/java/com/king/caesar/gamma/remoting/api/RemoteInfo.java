package com.king.caesar.gamma.remoting.api;

public class RemoteInfo
{
    private String address;
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    @Override
    public String toString()
    {
        return "RemoteInfo [address=" + address + "]";
    }
}
