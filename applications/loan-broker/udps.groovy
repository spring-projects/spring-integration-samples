//Groovy Script to catch multicast packets.
socket = new MulticastSocket(11111)
mcast = InetAddress.getByName("225.6.7.8")
socket.joinGroup(mcast)
buffer = (' ' * 1024) as byte[]
while(true) {
    incoming = new DatagramPacket(buffer, buffer.length)
    socket.receive(incoming)
    s = new String(incoming.data, 0, incoming.length)
    println ("** Shark **" + s)
}

