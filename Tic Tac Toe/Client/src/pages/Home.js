import { CreateRoom } from '../component/home/CreateRoom';
import JoinRoom from '../component/home/JoinRoom';
import { UserProfile } from '../component/home/UserProfile'
import '../styles/home.css';

const Home = () => {
  return (
    <div>
      <UserProfile />
      <div className='room'>
        <CreateRoom />
        <JoinRoom />
      </div>
    </div>
  );
};

export default Home;
